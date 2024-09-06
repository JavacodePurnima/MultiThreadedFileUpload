package com.example.monitor;

import com.example.entity.FilesEntity;
import com.example.repository.FilesRepository;
import com.example.services.FileFactory;
import com.example.services.FileProcessor;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

/**
 * Directory Monitoring Service.
 */
@Component
public class DirectorWatchService {

	@Autowired
	private FileFactory fileFactory;
	private  Path path;
	private  WatchService watchService;

	/** The registration key that represents the directory being monitored. */
	private  WatchKey key;

	/** A flag used to control the execution of the monitoring loop. */
	private boolean running = true;

	@Autowired
	private FilesRepository filesRepository;

	/**
	 * Initialises DirectorWatchService.
	 * @param dirPath
	 */
	@Autowired
	public DirectorWatchService(@Value("${dir.base.path}") String dirPath){
		try {
			path = Paths.get(dirPath);
			if (!Files.exists(path)) {
				throw new IllegalArgumentException("Directory path does not exist: " + dirPath);
			}

			FileSystem fileSystem = path.getFileSystem();
			watchService = fileSystem.newWatchService();
			key = path.register(watchService,
					StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_MODIFY,
					StandardWatchEventKinds.ENTRY_DELETE);

			System.out.println("Watching directory: " + path);
		} catch (IOException e) {
			System.err.println("Error setting up WatchService: " + e.getMessage());
		}
	}


	/**
	 * After the bean is created and all dependencies are injected, this method starts a separate thread to begin monitoring the directory.
	 * The thread will run as a daemon, meaning it won't block the application from shutting down.
	 */
	@PostConstruct
	public void  monitorFiles(){
		Thread t1=new Thread(() -> {
                processEvents();
        });
		t1.setDaemon(true);// Ensure the thread does not prevent the JVM from shutting down
		t1.start();
	}

	/**
	 * monitor changes in a directory on the file system.
	 * @throws IOException
	 * @throws InterruptedException
	 */

	public void processEvents() {
		//loop continues as long as the WatchKey is valid (i.e., the directory is being monitored).
		while (running){
            WatchKey watchKey= null;// returning the next WatchKey that has pending events.
            try {
                watchKey = watchService.take();

            for (WatchEvent<?> event : watchKey.pollEvents()) {//iterates over all events that have occurred.
				if(event.kind()==StandardWatchEventKinds.OVERFLOW){
					System.out.println("Overflow........");
				} else if (event.kind()==StandardWatchEventKinds.ENTRY_CREATE) {
					Path p= path.resolve((Path)event.context());
					System.out.println("CREATE FILE/DIR==>"+p.toAbsolutePath());
					fileProcess(p);
				}else if(event.kind()==StandardWatchEventKinds.ENTRY_MODIFY){
					Path p= path.resolve((Path)event.context());
					System.out.println("MODIFY FILE/DIR==>"+p.toAbsolutePath());
					fileProcess(p);
				}else {
					Path p= path.resolve((Path)event.context());
					System.out.println("DELETE FILE/DIR==>"+p.toAbsolutePath());
					deleteFileFromDB(p.getFileName());
				}
			}
			boolean valid = key.reset();
			if (!valid) {
				break;
			}
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

        }
	}

	private void deleteFileFromDB(Path fileName) {
		Optional<FilesEntity> byFileName = filesRepository.findByFileName(String.valueOf(fileName));
        byFileName.ifPresent(filesEntity -> filesRepository.delete(filesEntity));
	}

	@PreDestroy
	public void stopWatching() {
		running = false;
		try {
			watchService.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void fileProcess(Path p) {
		String fileName= p.getFileName().toString();
		String fileType=fileName.substring(fileName.lastIndexOf(".")+1);
		try {
			FileProcessor fileProcessor = fileFactory.getFileProcessor(fileType);
			fileProcessor.processFile(p);
		}catch (IllegalArgumentException e){
			System.out.println(e.getLocalizedMessage());
		}
	}

}
