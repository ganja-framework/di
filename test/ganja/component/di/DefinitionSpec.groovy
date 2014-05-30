package ganja.component.di

import spock.lang.Specification

class DefinitionSpec extends Specification {

    void "it is initialisable"() {

        given:
        def definition = new Definition()

        expect:
        definition instanceof Definition
    }
}