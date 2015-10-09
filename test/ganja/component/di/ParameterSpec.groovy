package ganja.component.di

import spock.lang.Specification

class ParameterSpec extends Specification {

    void "it is initialisable"() {

        given:
        def reference = new Parameter('db.user')

        expect:
        reference instanceof Parameter
    }

    void "it implements toString method"() {

        given:
        def reference = new Reference('service.id')

        expect:
        reference as String == 'service.id'
    }
}
