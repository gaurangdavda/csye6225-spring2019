package edu.neu.csye6225.spring19.cloudninja.service;

import java.util.List;
import java.util.UUID;

import edu.neu.csye6225.spring19.cloudninja.exception.UnAuthorizedLoginException;
import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.Note;

public interface NoteTakingService {

	public List<Note> getAllNotes(String auth) throws ValidationException, UnAuthorizedLoginException;

	public void createNote(String auth, Note note) throws ValidationException, UnAuthorizedLoginException;

	public Note getNote(String auth, UUID noteId) throws ValidationException, UnAuthorizedLoginException;

	public void updateNote(String auth, UUID noteId, Note note) throws ValidationException, UnAuthorizedLoginException;

	public void deleteNote(String auth, UUID noteId) throws ValidationException, UnAuthorizedLoginException;

}