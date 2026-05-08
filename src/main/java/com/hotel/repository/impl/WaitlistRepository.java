package com.hotel.repository.impl;

import com.hotel.model.WaitlistEntry;

public class WaitlistRepository extends BaseRepository<WaitlistEntry> {
    public WaitlistRepository() {
        super(WaitlistEntry.class);
    }
}
