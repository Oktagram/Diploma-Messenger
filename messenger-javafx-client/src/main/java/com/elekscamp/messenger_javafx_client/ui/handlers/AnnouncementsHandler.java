package com.elekscamp.messenger_javafx_client.ui.handlers;

import java.util.ArrayList;
import java.util.List;

import com.elekscamp.messenger_javafx_client.entities.Announcement;

public class AnnouncementsHandler {
	
	public List<Announcement> getSpreadedAnnouncements(List<Announcement> announcementsList, boolean getActive) {
		List<Announcement> result = new ArrayList<>();
		
		for (Announcement item : announcementsList) 
			if ((!item.getIsActive() && !getActive) 
					|| (item.getIsActive() && getActive)) 
				result.add(item);
		
		return result;
	}
}
