package ganja.component.di

class Reference {

    String serviceId

    Reference(String id) {
        serviceId = id.toLowerCase()
    }

    String toString() {
        serviceId
    }
}
