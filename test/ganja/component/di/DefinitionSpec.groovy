package ganja.component.di

import spock.lang.Specification

class DefinitionSpec extends Specification {

    void "it is initialisable"() {

        given:
        def definition = new Definition()

        expect:
        definition instanceof Definition
    }

    void "it can set constructor arguments"() {

        given:
        def definition = new Definition()

        when:
        definition.setArguments([property: 'value'])

        then:
        definition instanceof Definition
    }

    void "it can call any method on the object"() {

        given:
        def definition = new Definition()

        when:
        definition.calls('setSomeValue', 'some value')

        then:
        definition.methodCalls.size() == 1
    }

    void "it can be tagged"() {


        given:
        def definition = new Definition()

        when:
        definition.addTag('listener', [ event: 'some.event', method: 'onEvent' ])

        then:
        definition.hasTag('listener')
        definition.getTag('listener')['event'] == 'some.event'
        definition.getTag('listener')['method'] == 'onEvent'

        when:
        definition.clearTags()

        then:
        definition.getTags() == [:]

        when:
        definition.setTags([ [ name: 'listener', key: 'value'], [ name: 'my.tag', key: 'other value' ]])

        then:
        definition.getTags().size() == 2
        definition.hasTag('listener')
        definition.hasTag('my.tag')
        definition.getTag('listener')['key'] == 'value'

        when:
        definition.clearTag('my.tag')

        then:
        definition.hasTag('listener')
        ! definition.hasTag('my.tag')
    }
}