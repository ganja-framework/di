package ganja.component.di

import ganja.common.di.ContainerInterface
import org.slf4j.LoggerFactory

class Container implements ContainerInterface {

    ClassLoader classLoader

    Map<String, Definition> definitions = [:]
    Map<String, Object> services = [:]
    Map<String, String> parameters = [:]

    Definition register(String id, def classNameOrServiceId) {

        String className = getParameter(classNameOrServiceId) ?: classNameOrServiceId

        setDefinition(id.toLowerCase(), new Definition(className: className))
    }

    Definition setDefinition(String id, Definition definition) {

        definitions[id.toLowerCase()] = definition

        definition
    }

    def get(String id) {

        services[id.toLowerCase()] ?: createService(id.toLowerCase())
    }

    def get(Reference reference) {

        get(reference as String)
    }

    def createService(String id) {

        id = id.toLowerCase()

        if( ! classLoader) {
            classLoader = this.class.classLoader
        }

        if( ! definitions[id]) {
            throw new GroovyRuntimeException("Service definition '${id}' not found")
        }

        Definition d = definitions[id]
        def service

        if(d.arguments) {

            def arg = resolve(d.arguments)

            try {
                service = classLoader.loadClass(d.getClassName()).newInstance(arg)
            } catch (GroovyRuntimeException e) {
                service = classLoader.loadClass(d.getClassName()).newInstance(*arg)
            }

        }
        else {

            service = classLoader.loadClass(d.getClassName()).newInstance()
        }

        if(service.getMetaClass().respondsTo(service, 'setLogger')) {
            service.setLogger(LoggerFactory.getLogger(service.getClass()))
        }

        if(d.methodCalls.size()) {
            d.methodCalls.each({ String method, def arguments ->

                if(arguments) {

                    service."$method"(resolve(arguments))
                }
                else {
                    service."$method"()
                }
            })
        }

        services[id] = service
    }

    void setParameter(String id, def value) {

        parameters[id.toLowerCase()] = value
    }

    String getParameter(Parameter parameter) {

        getParameter(parameter as String)
    }

    String getParameter(String id) {

        parameters[id.toLowerCase()]
    }

    def resolve(def input) {

        if(input instanceof Iterable) {

            return input.collect { resolve(it) }
        }

        if(input instanceof Map) {

            return input.collectEntries { resolve(it) }
        }

        if(input instanceof Map.Entry) {

            if(input.value instanceof Reference) {
                input.setValue(get(input.value))
            }

            if(input.value instanceof Parameter) {
                input.setValue(getParameter(input.value))
            }

            return input
        }

        if(input instanceof Reference) {

            return get(input)
        }

        if(input instanceof Parameter) {
            return getParameter(input)
        }

        input
    }

    Map findServiceIdsByTag(String tag) {

        Map<String, Object> tags = [:]

        getDefinitions().each { String id, Definition item -> if(item.hasTag(tag)) { tags.put(id, item.getTag(tag)) } }

        tags
    }

    void injectService(String serviceId, def service) {

        register(serviceId, service.getClass().toString())
        services[serviceId] = service
    }
}
