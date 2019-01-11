package com.longfor.longjian.houseqm.app.vo.url;

import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.url
 * @ClassName: Url
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/10 20:45
 */
@Data
@NoArgsConstructor
public class Url implements Serializable {
    //Scheme     string
    //	Opaque     string    // encoded opaque data
    //	User       *Userinfo // username and password information
    //	Host       string    // host or host:port
    //	Path       string    // path (relative paths may omit leading slash)
    //	RawPath    string    // encoded path hint (see EscapedPath method)
    //	ForceQuery bool      // append a query ('?') even if RawQuery is empty
    //	RawQuery   string    // encoded query values, without '?'
    //	Fragment   string    // fragment for references, without '#'
    private String scheme;
    private String opaque;
    private Userinfo user;
    private String host;
    private String path;
    private String rawPath;
    private boolean forceQuery;
    private String rawQuery;
    private String fragment;

    public Url parse(String rawurl){
        List<String> uFrag = StringSplitToListUtil.splitToStringComma(rawurl, "#");
        String u = uFrag.get(0);
        String frag = uFrag.get(1);
        Url url = parse(u, false);
        if (frag.equals("")){
            return url;
        }

        //	if url.Fragment, err = unescape(frag, encodeFragment); err != nil {
        //		return nil, &Error{"parse", rawurl, err}
        //	}

        //if (url.getFragment())
        return url;
    }

    public Url parse(String rawurl,boolean viaRequest){

        return null;
    }
}
