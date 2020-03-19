package com.hujiang.project.zhgd.lyPersonnel.api;

import com.hujiang.framework.web.domain.AjaxResult;
import com.hujiang.project.zhgd.lyAttendanceRecord.domain.LyAttendanceRecord;
import com.hujiang.project.zhgd.lyPersonnel.domain.LyCompanyPersonnel;
import com.hujiang.project.zhgd.lyPersonnel.domain.LyPersonnel;
import com.hujiang.project.zhgd.lyPersonnel.domain.LyPersonnelRecord;
import com.hujiang.project.zhgd.lyPersonnel.service.ILyPersonnelService;
import com.hujiang.project.zhgd.lyRegistrationRecord.domain.LyRegistrationRecord;
import com.hujiang.project.zhgd.lyRegistrationRecord.mapper.LyRegistrationRecordMapper;
import com.hujiang.project.zhgd.lyRegistrationRecord.service.ILyRegistrationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/provider/lyPersonnel",method = RequestMethod.POST)
public class PersonnelApi {
    @Autowired
    private ILyPersonnelService lyPersonnelService;
    @Autowired
    private ILyRegistrationRecordService lyRegistrationRecordService;

    @PostMapping("/insertPersonnel")
    public AjaxResult insertPersonnel(@RequestBody LyPersonnel lyPersonnel){
        lyPersonnel.setIspresent("0");
        LyPersonnel a=new LyPersonnel();
        a.setPid(lyPersonnel.getPid());
        a.setIdCode(lyPersonnel.getIdCode());
        List<LyPersonnel> lList=lyPersonnelService.selectLyPersonnelList(a);
        int i=0;
        if(lList.size()>0){
            lyPersonnel.setId(lList.get(0).getId());
          i=  lyPersonnelService.updateLyPersonnel(lyPersonnel);
        }else{
           i= lyPersonnelService.insertLyPersonnel(lyPersonnel);
        }
        //登记记录
        LyRegistrationRecord lrr=new LyRegistrationRecord();
        lrr.setPwid(lyPersonnel.getId());
        lrr.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        lrr.setCompanyName(lyPersonnel.getCompanyName());
        lrr.setFloor(lyPersonnel.getFloor());
        lrr.setSubordinate(lyPersonnel.getSubordinate());
        lrr.setBz(lyPersonnel.getBz());
        lrr.setPid(lyPersonnel.getPid());
        lrr.setType(lyPersonnel.getType());
        lyRegistrationRecordService.insertLyRegistrationRecord(lrr);
        //添加人脸下发指令
        lyPersonnelService.personnelInOUt(lyPersonnel,"0");
        if(i>0){
            return AjaxResult.success("登记成功");
        }else{
            return AjaxResult.error("登记失败");
        }

    }

    /**
     * 信息查询人员资料
     */
    @PostMapping("/selectPersonnelCompany")
    public AjaxResult selectPersonnelCompany(@RequestBody LyPersonnel lyPersonnel){
        lyPersonnel.setType("1");
        lyPersonnel.setIspresent("0");
        List<LyCompanyPersonnel> lcpList=lyPersonnelService.getLyCompanyPersonnel(lyPersonnel);
        LyCompanyPersonnel l;
        for(int i=0;i<lcpList.size();i++){
           l=lcpList.get(i);
           l.setSize(l.getpList().size());
        }
            return AjaxResult.success(lcpList);
    }
    /**
    * 人员动态
     */
    @PostMapping("/getPersonnelDT")
    public AjaxResult getPersonnelDT(@RequestParam Integer pid,@RequestParam String time){
        Map<String,Map> resultMap=new HashMap<String,Map>();
        Map<String,Integer> zzMap=new HashMap<String,Integer>();
        Map<String,Integer> fkMap=new HashMap<String,Integer>();
        LyAttendanceRecord la=new LyAttendanceRecord();
        la.setProjectId(pid);
        la.setPassedTime(time);
        //在职人员总数
        Integer zzryzs=lyPersonnelService.zzryzs(pid);
        //在职人员进入数
        Integer zzryin=lyPersonnelService.zzryin(la);
        //在职人员出去数
        Integer zzryout=lyPersonnelService.zzryout(la);
        zzMap.put("zzryzs",zzryzs);
        zzMap.put("zzryin",zzryin);
        zzMap.put("zzryout",zzryout);
        resultMap.put("zz",zzMap);
        //访客总人数
        Integer fkryzs=lyPersonnelService.fkryzs(la);
        //访客进入人数
        Integer fkryin=lyPersonnelService.fkryin(la);
        //访客出去人数
        Integer fkryout=lyPersonnelService.fkryout(la);
        fkMap.put("fkryzs",fkryzs);
        fkMap.put("fkryin",fkryin);
        fkMap.put("fkryout",fkryout);
        resultMap.put("fk",fkMap);
        return AjaxResult.success(resultMap);
    }
    /**
     * 人员进出记录
     */
    @PostMapping("/getPersonnelRecord")
    public AjaxResult getPersonnelRecord(@RequestParam Integer pid,@RequestParam String time){

        LyAttendanceRecord lar=new LyAttendanceRecord();
        lar.setProjectId(pid);
        lar.setPassedTime(time);
        Map<String,Map> resultMap=new HashMap<String,Map>();
        Map<String,Object> zzMap=new HashMap<String,Object>();
        Map<String,Object> fkMap=new HashMap<String,Object>();
        List<LyPersonnelRecord> zzList=lyPersonnelService.getLyPersonnelRecordZZ(lar);
        List<LyPersonnelRecord> fkList=lyPersonnelService.getLyPersonnelRecordFK(lar);
        zzMap.put("zzList",zzList);
        zzMap.put("zzSize",zzList.size());
        resultMap.put("zzMap",zzMap);
        fkMap.put("fkList",fkList);
        fkMap.put("fkSize",fkList.size());
        resultMap.put("fkMap",fkMap);
        return AjaxResult.success(resultMap);




    }
    /**
     * 查询人员信息
     */
    @PostMapping("/selectPersonnelById")
    public AjaxResult selectPersonnelById(@RequestParam Integer personnelId ){
        LyPersonnel lyPersonnel=lyPersonnelService.selectLyPersonnelById(personnelId);
        return AjaxResult.success(lyPersonnel);

    }
}
