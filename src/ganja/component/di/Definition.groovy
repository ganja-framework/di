package ganja.component.di

class Definition {

    String className
    def arguments
    List<String> tags = []

    Definition setArguments(def args) {

        arguments = args

        this
    }

    void addTag(String tag) {

        if( ! tags.contains(tag)) {

            tags.add(tag)
        }
    }

    Boolean hasTag(String tag) {
        tags.contains(tag)
    }

    void clearTags() {
        tags = []
    }

    void clearTag(String tag) {

        tags.remove(tag)
    }
}
