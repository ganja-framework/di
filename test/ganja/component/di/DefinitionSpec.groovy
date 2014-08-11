package ganja.component.di

import spock.lang.Specification

class DefinitionSpec extends Specification {

    void "it is initialisable"() {

        given:
        def definition = new Definition()

        expect:
        definition instanceof Definition
    }

    void "it can set contructor arguments"() {

        given:
        def definition = new Definition()

        when:
        definition.setArguments([property: 'value'])

        then:
        definition instanceof Definition
    }
}