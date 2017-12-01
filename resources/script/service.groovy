import com.intellij.openapi.util.text.StringUtil

import CommonUtils

/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */
printOut = null
packageName = "com.sample"
importList = [
        'java.util.List;',
        'java.util.Map;'
]
showPk = 'PrimaryKey'
primaryKeyStr = ''

typeMapping = [
        (~/(?i)int/)                      : "int",
        (~/(?i)float|double|decimal|real/): "double",
        (~/(?i)datetime|timestamp/)       : "Timestamp",
        (~/(?i)date/)                     : "Date",
        (~/(?i)time/)                     : "Time",
        (~/(?i)/)                         : "String"
]
importMapping = [
        (~/(?i)datetime|timestamp/): "java.sql.Timestamp",
        (~/(?i)date/)              : "java.sql.Date",
        (~/(?i)time/)              : "java.sql.Time",
]
packageMapping = [
        (~/(?i)int/)                      : "Integer",
        (~/(?i)float|double|decimal|real/): "Double",
        (~/(?i)datetime|timestamp/)       : "Timestamp",
        (~/(?i)date/)                     : "Date",
        (~/(?i)time/)                     : "Time",
        (~/(?i)/)                         : "String"
]


ServiceObject = {
    name: ''
    lowName: ''
    serviceName: ''
    servicePath: ''
    serviceImplName: ''
    serviceImplPath: ''
    dtoName: ''
    dtoPath: ''
    modelName: ''
    modelPath: ''

}

if (configInfo != null) {
    packageName = configInfo.getPackagePath()
}

ServiceObject.name = javaName(table.getTableName(), true)
ServiceObject.lowName = javaName(table.getTableName(), false)

ServiceObject.serviceName = ServiceObject.name + "Service"
ServiceObject.servicePath = packageName + ".service"

ServiceObject.serviceImplName = ServiceObject.serviceName + "Impl"
ServiceObject.serviceImplPath = ServiceObject.servicePath + ".impl"

ServiceObject.dtoName = ServiceObject.name + "DTO"
ServiceObject.dtoPath = packageName + ".dto"

ServiceObject.modelName = ServiceObject.name + "Model"
ServiceObject.modelPath = packageName + ".model"

utils = new CommonUtils()

generate(table, dir)

def generate(table, dir) {

    def serviceImplFile = new File(dir + "/service/impl")
    serviceImplFile.mkdirs()

    table.primaryColumns.each { it ->
        primaryKeyStr += getField(it.type) + " " + it.field + ","
        def importPath = getImport(it.type)
        if (importPath != null) importList += [importPath.value + ';']
    }
    if (table.primaryColumns.size() == 1) {
        showPk = javaName(table.primaryColumns[0].field, true)
    }
    primaryKeyStr = primaryKeyStr.getAt(0..primaryKeyStr.size() - 2)

//    new File(serviceImplFile.getPath(), ServiceObject.serviceImplName + ".java").withPrintWriter { out ->
//        printOut = out
//        generateImpl()
//    }
    new File(serviceImplFile.getParent(), ServiceObject.serviceName + ".java").withPrintWriter { out ->
        printOut = out
//        utils.printOut = out
        generateService()
    }

//    utils.run()
}

def generateService() {
    write("package $servicePath;")
    write("")

    write("import $dtoPath.$dtoName" + ";")
    importList.each() { it ->
        write("import $it")
    }
    write("")

    write("public interface $serviceName {")
    write("")

    /**insert**/
    write("int save$name($dtoName $lowName);", 1).write("")

    /**update**/
    write("boolean update$name($dtoName $lowName);", 1).write("")

    /**delete**/
    write("boolean delete" + name + "By$showPk(" + primaryKeyStr + ");", 1).write("")

    /**get**/
    write("$dtoName get$dtoName" + "By$showPk($primaryKeyStr);", 1).write("")

    /**getAll**/
    write("List<$dtoName> getAll$dtoName();", 1).write("")

    if (table.primaryColumns.size() == 1) {
        /**List**/
        write("List<$dtoName> get$dtoName" + "ListBy$showPk(List<" + getPackageField(table.primaryColumns[0].type) + "> " + table.primaryColumns[0].field + "List);",1).write("")

        /**Map**/
        write("Map<" + getPackageField(table.primaryColumns[0].type) + ",$dtoName> get$dtoName" + "MapBy$showPk(List<" + getPackageField(table.primaryColumns[0].type) + "> " + table.primaryColumns[0].field + "List);", 1).write("")
    }

    write("}")
}

def generateImpl() {
    write("package $serviceImplPath;")
    write("")

    write("import $servicePath.$serviceName" +";")
    write("import $dtoPath.$dtoName" + ";")
    importList.each() { it ->
        write("import $it")
    }
    write("")

    write("public class $serviceImplName implements $serviceName {")
    write("")

    /**insert**/
    write("@Override",1)
    write("public int save$name($dtoName $lowName){", 1)
    write("return 0;",2)
    write("}",1).write("")

    /**update**/
    write("@Override",1)
    write("public boolean update$name($dtoName $lowName){", 1)
    write("return false;",2)
    write("}",1).write("")

    /**delete**/
    write("@Override",1)
    write("public boolean delete" + name + "By$showPk(" + primaryKeyStr + "){", 1)
            .write("return false;",2)
            .write("}",1)
            .write("")

    /**get**/
    write("@Override",1)
    write("public $dtoName get$dtoName" + "By$showPk($primaryKeyStr){", 1)
            .write("return null;",2)
            .write("}",1)
            .write("")

    /**getAll**/
    write("@Override",1)
    write("public List<$dtoName> getAll$dtoName(){", 1)
            .write("return null;",2)
            .write("}",1)
            .write("")

    if (table.primaryColumns.size() == 1) {
        /**List**/
        write("@Override",1)
        write("public List<$dtoName> get$dtoName" + "ListBy$showPk(List<" + getPackageField(table.primaryColumns[0].type) + "> " + table.primaryColumns[0].field + "List){",1)
                .write("return null;",2)
                .write("}",1)
                .write("")

        /**Map**/
        write("@Override",1)
        write("public Map<" + getPackageField(table.primaryColumns[0].type) + ",$dtoName> get$dtoName" + "MapBy$showPk(List<" + getPackageField(table.primaryColumns[0].type) + "> " + table.primaryColumns[0].field + "List){", 1)
                .write("return null;",2)
                .write("}",1)
                .write("")
    }

    write("}")
}


def getField(spec) {
    def typeStr = typeMapping.find { p, t ->
        p.matcher(spec).find()
    }.value
}

def getPackageField(spec) {
    def typeStr = packageMapping.find { p, t ->
        p.matcher(spec).find()
    }.value
}

def getImport(spec) {
    def importPath = importMapping.find { p, t -> p.matcher(spec).find() }
}

def write(content, tabLength) {
    tabLength.times { content = "    " + content }
    printOut.println content
//    utils.printOut.println content
    return this
}

def write(content) {
    write(content, 0)
    return this
}

def javaName(str, capitalize) {
    def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
            .collect { StringUtil.toLowerCase(it).capitalize() }
            .join("")
            .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
    capitalize || s.length() == 1 ? s : StringUtil.toLowerCase(s[0]) + s[1..-1]
}

