package ganja.component.di

class Definition {

    String className
    def arguments
    Map<String, Object> tags = [:]

    Definition setArguments(def args) {

        arguments = args

        this
    }

    void setTags(List tags) {

        tags.each { Map item ->

            String name = item?.name

            if(name) {

                Map attr = [:]

                item.each { key, value ->

                    if(key != 'name') {

                        attr.put(key, value)
                    }
                }

                addTag(name, attr)
            }
        }
    }

    void addTag(String tag, Object attributes) {

        if( ! tags.keySet().contains(tag)) {

            tags.put(tag, attributes)
        }
    }

    Boolean hasTag(String tag) {
        tags.keySet().contains(tag)
    }

    void clearTags() {
        tags = [:]
    }

    void clearTag(String tag) {

        tags.remove(tag)
    }

    def getTag(String tag) {

        if(hasTag(tag)) {
            tags[tag]
        }
    }
}
