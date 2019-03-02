package com.longfor.longjian.houseqm.domain.internalservice;

import com.longfor.longjian.houseqm.po.zj2db.FileResource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Houyan
 * @date 2018/12/21 0021 17:13
 */
public interface FileResourceService {

    /**
     * @param attachments
     * @return java.util.List<com.longfor.longjian.houseqm.po.zj2db.FileResource>
     * @author hy
     * @date 2018/12/21 0021
     */
    List<FileResource> searchFileResourceByFileMd5InAndNoDeleted(List<String> attachments);

    List<FileResource> searchByMd5In(ArrayList<String> attachmentMd5List);
}
