package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.zj2db.FileResource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Houyan
 * @date 2018/12/21 0021 17:13
 */
public interface FileResourceService {

    /**
     *
     * @author hy
     * @date 2018/12/21 0021
     * @param attachments
     * @return java.util.List<com.longfor.longjian.houseqm.po.zj2db.FileResource>
     */
    List<FileResource> searchFileResourceByFileMd5InAndNoDeleted(List<String> attachments);

    List<FileResource> SearchByMd5In(ArrayList<String> attachmentMd5List);
}
