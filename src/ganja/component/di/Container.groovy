package ganja.component.di

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

        if(input instanceof Reference) {
            return get(input as String)
        }

        input
    }
}
