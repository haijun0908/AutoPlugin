import com.intellij.openapi.util.text.StringUtil



/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */
printOut = null
fields = []
packageName = "com.sample"
typeMapping = [
        (~/(?i)int/)                      : "int",
        (~/(?i)float|double|decimal|real/): "double",
        (~/(?i)datetime|timestamp/)       : "Timestamp",
        (~/(?i)date/)                     : "Date",
        (~/(?i)time/)                     : "Time",
        (~/(?i)/)                         : "String"
]
importMapping = [
        (~/(?i)datetime|timestamp/)       : "java.sql.Timestamp",
        (~/(?i)date/)                     : "java.sql.Date",
        (~/(?i)time/)                     : "java.sql.Time",
]
importList = [
        'java.io.Serializable'
]


if(configInfo != null) packageName = configInfo.getPackagePath()
generate(table,dir)

def generate(table, dir) {
    dir += '/model'
    new File(dir).mkdirs()
    def className = javaName(table.getTableName(), true)
    def fields = calcFields(table)
    new File(dir, className + "Model.java").withPrintWriter { out ->
        printOut = out
        generateFile(className, fields)
    }
}

def generateFile(className, fields) {
    printlnWithTab("package $packageName" + ".model;")
    printlnWithTab("")
    if(importList != null) importList.each{path -> printlnWithTab("import " + path + ";")}
    printlnWithTab("")
    printlnWithTab("public class "+className+"Model implements Serializable {")
    printlnWithTab("")
    printlnWithTab("private static final long serialVersionUID = 1L;" , 1)
    printlnWithTab("")
    fields.each() {
        if (it.annos != "") printlnWithTab("${it.annos}")
        if (it.comment != '') {
            printlnWithTab("/**", 1)
            printlnWithTab(" * ${it.comment}", 1)
            printlnWithTab(" */", 1)
        }
        printlnWithTab("private ${it.type} ${it.name};", 1)
        printlnWithTab("")
    }
    fields.each() {
        printlnWithTab("")
        printlnWithTab("public ${it.type} get${it.name.capitalize()}() {", 1)
        printlnWithTab("return ${it.name};", 2)
        printlnWithTab("}", 1)
        printlnWithTab("")
        printlnWithTab("public void set${it.name.capitalize()}(${it.type} ${it.name}) {", 1)
        printlnWithTab("this.${it.name} = ${it.name};", 2)
        printlnWithTab("}", 1)
        printlnWithTab("")
    }
    printlnWithTab("}")
}
def printlnWithTab(content, tabLength) {
    tabLength.times{content = "    " + content}
    printOut.println content
}

def printlnWithTab(content) {
    printlnWithTab(content , 0)
}


def calcFields(table) {
    table.getColumnInfoList().each { col ->
        def spec = StringUtil.toLowerCase(col.getType())
        def typeStr = typeMapping.find { p, t ->
            p.matcher(spec).find() }.value
        def importPath = importMapping.find {p , t -> p.matcher(spec).find() }
        fields += [[
                           name   : javaName(col.getField(), false),
                           type   : typeStr,
                           comment: col.getComment(),
                           annos  : ""]]
        if(importPath != null && importList.indexOf(importPath.value) == -1)
        importList += [importPath.value]
    }
    return fields
}

def javaName(str, capitalize) {
    def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
            .collect { StringUtil.toLowerCase(it).capitalize() }
            .join("")
            .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
    capitalize || s.length() == 1 ? s : StringUtil.toLowerCase(s[0]) + s[1..-1]
}
