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

    void "it can be tagged"() {


        given:
        def definition = new Definition()

        when:
        definition.addTag('listener')

        then:
        definition.hasTag('listener')

        when:
        definition.clearTags()

        then:
        definition.getTags() == []

        when:
        definition.setTags([ 'listener', 'my.tag' ])

        then:
        definition.getTags() == [ 'listener', 'my.tag' ]
        definition.hasTag('listener')
        definition.hasTag('my.tag')

        when:
        definition.clearTag('my.tag')

        then:
        definition.hasTag('listener')
        ! definition.hasTag('my.tag')
    }
}