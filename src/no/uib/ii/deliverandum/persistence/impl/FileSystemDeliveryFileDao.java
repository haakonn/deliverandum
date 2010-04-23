package no.uib.ii.deliverandum.persistence.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lombok.NonNull;
import no.uib.ii.deliverandum.beans.DeliveredFile;
import no.uib.ii.deliverandum.beans.Delivery;
import no.uib.ii.deliverandum.persistence.DeliveryFileDao;
import no.uib.ii.deliverandum.persistence.exceptions.DaoException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class FileSystemDeliveryFileDao implements DeliveryFileDao {

    private static final String NOTES_PREFIX = "___";
    private static final String NOTES_POSTFIX = ".txt";
    
    @Value("${app.filedir}") private String fileDir;
    
    private File resolveFilePath(Delivery delivery, String filename) {
        String dir = resolvePath(delivery);
        File file = new File(dir);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new DaoException("Unable to create directory " + file);
            }
        }
        file = new File(dir + filename);
        if (file.exists()) {
            for (int i = 1; file.exists(); i++) {
                String newFilename = dir + i + "_" + filename;
                file = new File(newFilename);
            }
        }
        return file;
    }

    private String resolvePath(Delivery delivery) {
        return (fileDir +
                File.separatorChar +
                delivery.getAssignment().getCourseName() +
                File.separatorChar +
                delivery.getAssignment().getId() +
                File.separatorChar +
                delivery.getDeliveredBy().getUsername() +
                File.separatorChar);
    }
    
    private File resolveNotesPath(File file) {
        return new File(file.getParent(), NOTES_PREFIX + file.getName() + NOTES_POSTFIX);
    }
    
    @SuppressWarnings("unchecked") // because of LastModifiedFileComparator
    @Override
    public void addFiles(Delivery delivery) {
        File dir = new File(resolvePath(delivery));
        IOFileFilter notesFilter = new RegexFileFilter("^" + NOTES_PREFIX + ".*" + NOTES_POSTFIX + "$");
        IOFileFilter notNotesFilter = new NotFileFilter(notesFilter);
        if (!dir.exists()) {
            return;
        }
        Collection<File> files = FileUtils.listFiles(dir, notNotesFilter, null);
        Collections.sort((List<File>) files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        for (File file : files) {
            DeliveredFile dfile = new DeliveredFile();
            dfile.setFile(file);
            File notesFile = new File(file.getParent(), NOTES_PREFIX + file.getName() + NOTES_POSTFIX);
            if (notesFile.exists()) {
                try {
                    dfile.setNotes(FileUtils.readFileToString(notesFile));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new DaoException(e);
                }
            }
            delivery.getFiles().add(dfile);
        }
    }
    
    private String normalizeFilename(String filename) {
        filename = FilenameUtils.getName(filename); // strip any path away
        filename = filename.replace('_', '-');
        return filename;
    }

    @Override
    public void persist(
            @NonNull InputStream in,
            @NonNull String suggestedFilename,
            String notes,
            @NonNull Delivery delivery) {
        suggestedFilename = normalizeFilename(suggestedFilename);
        File file = resolveFilePath(delivery, suggestedFilename);
        try {
            IOUtils.copy(in, FileUtils.openOutputStream(file));
            if (notes != null) {
                File notesFile = resolveNotesPath(file);
                FileUtils.writeStringToFile(notesFile, notes);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DaoException(e);
        }
    }

}
