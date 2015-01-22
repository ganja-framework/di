package ganja.component.di

import org.slf4j.LoggerFactory

class Container {

    ClassLoader classLoader

    Map<String, Definition> definitions = [:]
    Map<String, Object> services = [:]
    Map<String, String> parameters = [:]

    Definition register(String id, String classNameOrServiceId) {

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

            def arg = resolveServices(d.arguments)

            try {
                service = classLoader.loadClass(d.getClassName()).newInstance(arg)
            } catch (GroovyRuntimeException e) {
                service = classLoader.loadClass(d.getClassName()).newInstance(*arg)
            }

        }
        else {

            service = classLoader.loadClass(d.getClassName()).newInstance()
        }

        if(service.metaClass.respondsTo(service, 'setLogger')) {
            service.setLogger(LoggerFactory.getLogger(service.getClass()))
        }

        services[id] = service
    }

    void setParameter(String id, String value) {

        parameters[id.toLowerCase()] = value
    }

    String getParameter(String id) {

        parameters[id.toLowerCase()]
    }

    def resolveServices(def input) {

        if(input instanceof Iterable) {

            return input.collect { resolveServices(it) }
        }

        if(input instanceof Map) {

            return input.collectEntries { resolveServices(it) }
        }

        if(input instanceof Map.Entry && input.value instanceof Reference) {

            input.setValue(get(input.value))
            return input
        }

        if(input instanceof Reference) {

            return get(input)
        }

        input
    }

    def findTaggedServiceIds(String tag) {

        Map<String, Object> tags = [:]

        getDefinitions().each { String id, Definition item -> if(item.hasTag(tag)) { tags.put(tag, item.getTag(tag)) } }

        tags
    }

//    void debug() {
//
//        println "Services"
//        services.each { println it}
//
//        println "Parameters"
//        parameters.each { println it }
//    }
}
