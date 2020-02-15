package dev.mdb.notebook;

/** A model class for a Note object. */

public class Note {

    private String noteName;
    private String noteContents;

    public Note(String name, String contents) {
        this.noteName = name;
        this.noteContents = contents;
    }

    public String getName() {
        return noteName;
    }

    public void setName(String noteName) {
        this.noteName = noteName;
    }

    public String getContents() {
        return noteContents;
    }

    public void setContents(String noteContents) {
        this.noteContents = noteContents;
    }
}
