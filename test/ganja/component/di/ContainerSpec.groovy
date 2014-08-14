package ganja.component.di

import spock.lang.Specification

class ContainerSpec extends Specification {

    void "it is initialisable"() {

        given:
        def container = new Container()

        expect:
        container instanceof Container
    }

    void "it can register service with std constructor and param argument"() {

        given:
        Container container = new Container()

        when:
        container.setParameter('service.class', 'java.util.EventObject')
        container
            .register('service', 'service.class')
            .setArguments(new Reference('service.param'))

        container
            .register('service.param', 'java.lang.Integer')
            .setArguments(3)

        def subject = container.get('service')

        then:
        subject instanceof EventObject
    }

    void "it can register service with std constructor and 2 or more param arguments"() {

        given:
        Container container = new Container()

        when:
        container.setParameter('service.class', 'java.util.Date')
        container
            .register('service', 'service.class')
            .setArguments([
                new Reference('service.param.year'),
                new Reference('service.param.month'),
                new Reference('service.param.day')
            ])

        container
            .register('service.param.year', 'java.lang.Integer')
            .setArguments(111)

        container
            .register('service.param.month', 'java.lang.Integer')
            .setArguments(2)

        container
            .register('service.param.day', 'java.lang.Integer')
            .setArguments(25)

        def subject = container.get('service')

        then:
        subject instanceof Date
    }

    void "it can register service with nested references"() {

        given:
        Container container = new Container()

        when:
        container.setParameter('service.class', 'java.util.EventObject')
        container.setParameter('sub.service.class', 'java.util.Date')

        container
            .register('service', 'service.class')
            .setArguments(new Reference('sub.service'))


        container
            .register('sub.service', 'sub.service.class')
            .setArguments([
                new Reference('year'),
                new Reference('month'),
                new Reference('day')
            ])

        container
            .register('year', 'java.lang.Integer')
            .setArguments(111)

        container
            .register('month', 'java.lang.Integer')
            .setArguments(2)

        container
            .register('day', 'java.lang.Integer')
            .setArguments(25)

        def subject = container.get('service')

        then:
        subject instanceof EventObject
    }

    void "it can register service with nested references in Map"() {

        given:
        Container container = new Container()

        when:
        container.register('sub.service', 'java.util.Date')
        container
            .register('service', 'java.util.EventObject')
            .setArguments([ start: new Reference('sub.service'), end: new Reference('sub.service') ])

        def subject = container.get('service')

        then:
        subject instanceof EventObject
    }

//    void "it throws exception on circular reference"() {
//
//
//    }
}