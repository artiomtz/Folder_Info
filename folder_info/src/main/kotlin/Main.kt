import java.util.Date
import java.io.File
import java.text.SimpleDateFormat

fun folderSize(directory: File, files: MutableList<FileInfo>) : Long {
    var directorySize = 1L
    val directoryFiles: Array<File>?

    // verify subdirectory can be read
    try {
        directoryFiles = directory.listFiles()
    } catch (e: Exception) {
        return 0
    }
    // traverse subdirectory
    for (file in directoryFiles) {
        val lastModified = Date(file.lastModified())
        val formatter = SimpleDateFormat("dd/MM/yyyy, HH:mm")
        val lastModifiedString = formatter.format(lastModified)

        // item is a file
        if (file.isFile) {
            val fileSize = file.length()
            directorySize += fileSize
            files.add(FileInfo(path = file.path, dateModified = lastModifiedString, size = fileSize, isFile = true))
        }
        // item is a folder
        else {
            val folderSize = folderSize(file, files)
            directorySize += folderSize
            files.add(FileInfo(path = file.path, dateModified = lastModifiedString, size = folderSize, isFile = false))
        }
    }
    return directorySize
}

fun printResults(files: MutableList<FileInfo>, numFolders: Int, directorySize: Long) {
    print("The directory has ${files.size} items ($numFolders folders, ${files.size - numFolders} files) ")
    println("and a total size of " + "%,d".format(directorySize) + " bytes.")

    // print header
    System.out.format("-".repeat(200) + "\n")
    System.out.format("| Size In Bytes        | Last Modification         | Path%n")
    System.out.format("-".repeat(200) + "\n")

    // print results
    for (file in files) {
        System.out.format("| %-20s | %-25s | %-145s |%n", "%,d".format(file.size), file.dateModified, file.path)
    }
}

fun main() {
    println("This program will provide some basic information about a source folder.")
    var directory = File("")

    // wait for a correct path from console
    while(!directory.exists()) {
        println("Please enter a valid path: (i.e. C:\\Users\\Dave\\Pictures)\n")
        val path = readLine().toString()
        directory = File(path)
    }
    // traverse path
    val files = mutableListOf<FileInfo>()
    val directorySize = folderSize(directory, files)

    // sort and print results
    val numFolders = files.count { !it.isFile }
    files.sortByDescending { it.size }
    printResults(files, numFolders, directorySize)
}
