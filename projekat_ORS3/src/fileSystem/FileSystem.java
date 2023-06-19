package fileSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.scene.control.TreeItem;
import memory.StoredFile;
import shell.Shell;

public class FileSystem {
	private static File rootFolder;
	private static File currentFolder;
	private TreeItem<File> treeItem;

	public FileSystem(File path) {
		rootFolder = path;
		currentFolder = rootFolder;
		treeItem = new TreeItem<>(rootFolder);
		createTree(treeItem);
	}
	
	public TreeItem<File> getTreeItem() {
		treeItem = new TreeItem<>(currentFolder);
		createTree(treeItem);
		return treeItem;
	}
	
	public void createTree(TreeItem<File> rootItem) {
		try (DirectoryStream<Path> directoryStream = 
				Files.newDirectoryStream(Paths.get(rootItem.getValue().getAbsolutePath()))) {
			for (Path path: directoryStream) {
				TreeItem<File> newItem = new TreeItem<>(path.toFile());
				newItem.setExpanded(false);
				rootItem.getChildren().add(newItem);
				if (Files.isDirectory(path))
					createTree(newItem);
				else {
					byte[] elements = Files.readAllBytes(newItem.getValue().toPath());
					StoredFile newFile = new StoredFile(newItem.getValue().getName(), elements);
					if (!Shell.disk.contains(newItem.getValue().getName()))
						Shell.disk.storeFile(newFile);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void listFiles() {
		System.out.println("Current folder " + currentFolder.getName() + " contains:");
		System.out.println("Type\t\tName");
		for (TreeItem<File> file: Shell.tree.getTreeItem().getChildren()) {
			if (file.getValue().isDirectory())
				System.out.println("Folder\t\t" + file.getValue().getName());
			else
				System.out.println("File\t\t" + file.getValue().getName());
		}
	}
	
	public static void changeDirectory(String directory) {
		if (directory.equals("..") && !currentFolder.equals(rootFolder))
			currentFolder = currentFolder.getParentFile();
		else 
			for (TreeItem<File> file: Shell.tree.getTreeItem().getChildren()) {
				if (file.getValue().getName().equals(directory) && file.getValue().isDirectory())
					currentFolder = file.getValue();
			}
	}
	
}
