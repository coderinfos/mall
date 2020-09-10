package org.coderinfo.pf.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author fenggang
 */
public interface BaseRepository<T, ID>  extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}
