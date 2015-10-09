package ganja.component.di.parser

import ganja.common.di.ContainerInterface
import ganja.common.di.DefinitionInterface
import ganja.component.di.Reference

class DefinitionParser {

    void parse(Map input, ContainerInterface container) {

        input?.parameters?.each({ def key, def value ->
            container.setParameter(key, value)
        })

        input?.services?.each({ String serviceId, Map config ->

            if(config?.class) {

                DefinitionInterface definition = container.register(serviceId, config?.class)

                if(config?.arguments) {

                    def args

                    if(config?.arguments instanceof Map) {

                        args = [:]

                        config?.arguments?.each({ String property, String value ->
                            if(value.startsWith('$')) {
                                args.put(property, new Reference(value.substring(1)))
                            }
                            else {
                                args.put(property, value)
                            }
                        })
                    }

                    if(config?.arguments instanceof List) {

                        args = []

                        config?.arguments?.each({ String value ->
                            if(value.startsWith('$')) {
                                args << new Reference(value.substring(1))
                            }
                            else {
                                args << value
                            }
                        })
                    }

                    definition.setArguments(args)
                }

                if(config?.calls && config?.calls?.size()) {

                    for(item in config?.calls) {

                        if(item?.arguments && item?.arguments?.startsWith('$')) {

                            definition.calls(item.method, new Reference(item?.arguments?.substring(1)))
                        }
                        else {

                            definition.calls(item.method, item?.arguments)
                        }
                    }
                }

                if(config?.tags) {
                    definition.setTags(config?.tags)
                }
            }
        })
    }
}
