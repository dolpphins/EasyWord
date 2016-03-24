package com.mao.eventbus;

import com.mao.bean.Note;

/**
 * EventBus对象
 * 
 * @author mao
 *
 */
public class NoteEntry {

	private Note preNote;
	
	private Note newNote;

	public NoteEntry() {}
	
	public NoteEntry(Note preNote, Note newNote) {
		this.preNote = preNote;
		this.newNote = newNote;
	}
	
	public Note getPreNote() {
		return preNote;
	}

	public void setPreNote(Note preNote) {
		this.preNote = preNote;
	}

	public Note getNewNote() {
		return newNote;
	}

	public void setNewNote(Note newNote) {
		this.newNote = newNote;
	}
}
