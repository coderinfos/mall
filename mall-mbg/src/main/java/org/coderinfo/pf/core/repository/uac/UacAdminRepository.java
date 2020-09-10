package org.coderinfo.pf.core.repository.uac;

import org.coderinfo.pf.core.domain.uac.UacAdmin;
import org.coderinfo.pf.core.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author fenggang
 */
public interface UacAdminRepository extends BaseRepository<UacAdmin, Long> {
}
