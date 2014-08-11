package ganja.component.di

import spock.lang.Specification

class ReferenceSpec extends Specification {

    void "it is initialisable"() {

        given:
        def reference = new Reference('service.id')

        expect:
        reference instanceof Reference
    }

    void "it implements toString method"() {

        given:
        def reference = new Reference('service.id')

        expect:
        reference as String == 'service.id'
    }
}