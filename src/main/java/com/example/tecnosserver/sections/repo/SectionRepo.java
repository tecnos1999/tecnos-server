package com.example.tecnosserver.sections.repo;

import com.example.tecnosserver.page.model.Page;
import com.example.tecnosserver.sections.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SectionRepo extends JpaRepository<Section, Long> {


    List<Section> findByPage(Page page);


}
