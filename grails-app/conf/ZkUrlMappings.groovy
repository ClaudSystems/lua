class ZkUrlMappings {

    static excludes = ['/zkau/*', '/zkcomet/*']

    static mappings = {
        "/" ( controller:'home', action:'index' )
    }

}
