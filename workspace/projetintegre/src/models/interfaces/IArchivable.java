package models.interfaces;

import java.util.Date;

public interface IArchivable {
	void archiver();

	boolean isArchive();

	Date getDateArchivage();
}
