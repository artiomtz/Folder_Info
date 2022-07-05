import java.util.*
import java.io.File
import java.text.SimpleDateFormat

fun folderSize(directory: File, files: MutableList<FileInfo>) : Long {
    var directorySize = 1L
    for (file in directory.listFiles()) {

        val lastModified = Date(file.lastModified())
        val formatter = SimpleDateFormat("dd/MM/yyyy, HH:mm")
        val lastModifiedString = formatter.format(lastModified)

        if (file.isFile) {
            val fileSize = file.length()
            directorySize += fileSize
            files.add(FileInfo(path = file.path, dateModified = lastModifiedString, size = fileSize))
        }
        else {
            val folderSize = folderSize(file, files)
            directorySize += folderSize
            files.add(FileInfo(path = file.path, dateModified = lastModifiedString, size = folderSize))
        }
    }
    return directorySize
}

fun main() {
    println("This program will provide some basic information about a source folder.")
    print("Please enter a path: (i.e. C:\\Users\\Dave\\Pictures) \n\n")
    val path = "D:\\University\\4B\\REC 100"
//    val path = "D:\\University\\Resume"
//    val path = "C:\\Users\\Art\\Pictures"
//    val path = readLine()
    ///////// separate printing function

    val files = mutableListOf<FileInfo>()
    val directory = File(path) //////// check path exists
    val directorySize = folderSize(directory, files)
    files.sortByDescending { it.size }

    // print results
    println("The directory has ${files.size} items, and a total size of " + "%,d".format(directorySize) + " bytes.") ////////// separate files/folders count
    System.out.format("-".repeat(200) + "\n")
    System.out.format("| Size In Bytes        | Last Modification         | Path%n")
    System.out.format("-".repeat(200) + "\n")
    for (file in files) {
        System.out.format("| %-20s | %-25s | %-145s |%n", "%,d".format(file.size), file.dateModified, file.path)
    }
}
////////// comments