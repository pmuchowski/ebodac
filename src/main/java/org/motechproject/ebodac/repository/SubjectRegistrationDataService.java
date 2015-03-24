package org.motechproject.ebodac.repository;

import org.motechproject.ebodac.domain.SubjectRegistration;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * MotechDataService base class will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.  In this class we
 * define and custom lookups we may need.
 */
public interface SubjectRegistrationDataService extends MotechDataService<SubjectRegistration> {
    @Lookup
    SubjectRegistration findRegistrationByFirstName(@LookupField(name = "firstName") String firstName);
}