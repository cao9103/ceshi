package com.ruoyi.project.ht.appraise.controller;

import java.util.List;

import com.ruoyi.project.ht.commodity.domain.DCommodity;
import com.ruoyi.project.ht.commodity.service.IDCommodityService;
import com.ruoyi.project.ht.merchant.domain.DMerchant;
import com.ruoyi.project.ht.merchant.service.IDMerchantService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.ht.appraise.domain.DAppraise;
import com.ruoyi.project.ht.appraise.service.IDAppraiseService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 商品评价Controller
 * 
 * @author ruoyi
 * @date 2021-12-14
 */
@Controller
@RequestMapping("/ht/appraise")
public class DAppraiseController extends BaseController
{
    private String prefix = "ht/appraise";

    @Autowired
    private IDAppraiseService dAppraiseService;
    @Autowired
    private IDMerchantService idMerchantService;
    @Autowired
    private IDCommodityService idCommodityService;

    @RequiresPermissions("ht:appraise:view")
    @GetMapping()
    public String appraise()
    {
        return prefix + "/appraise";
    }

    /**
     * 查询商品评价列表
     */
    @RequiresPermissions("ht:appraise:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(DAppraise dAppraise)
    {
        startPage();
        List<DAppraise> list = dAppraiseService.selectDAppraiseList(dAppraise);
        for (DAppraise appraise : list) {
            DCommodity dCommodity=idCommodityService.selectDCommodityById(appraise.getCommodityid());
           // System.out.println("dCommodity+++"+dCommodity.getName());
            if(dCommodity!=null){
                appraise.setMerchantname(dCommodity.getName());
            }else{
                appraise.setMerchantname("");
            }
        }
        return getDataTable(list);
    }

    /**
     * 导出商品评价列表
     */
    @RequiresPermissions("ht:appraise:export")
    @Log(title = "商品评价", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(DAppraise dAppraise)
    {
        List<DAppraise> list = dAppraiseService.selectDAppraiseList(dAppraise);
        ExcelUtil<DAppraise> util = new ExcelUtil<DAppraise>(DAppraise.class);
        return util.exportExcel(list, "商品评价数据");
    }

    /**
     * 新增商品评价
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存商品评价
     */
    @RequiresPermissions("ht:appraise:add")
    @Log(title = "商品评价", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(DAppraise dAppraise)
    {
        return toAjax(dAppraiseService.insertDAppraise(dAppraise));
    }

    /**
     * 修改商品评价
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        DAppraise dAppraise = dAppraiseService.selectDAppraiseById(id);


        DCommodity commodity=new DCommodity();
        // dGoodssort.setParentId((long)0);
        List <DCommodity> list=idCommodityService.selectDCommodityList(commodity);
        mmap.put("DGoodssortone", list);


        mmap.put("dAppraise", dAppraise);
        return prefix + "/edit";
    }

    /**
     * 修改保存商品评价
     */
    @RequiresPermissions("ht:appraise:edit")
    @Log(title = "商品评价", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(DAppraise dAppraise)
    {
        return toAjax(dAppraiseService.updateDAppraise(dAppraise));
    }

    /**
     * 删除商品评价
     */
    @RequiresPermissions("ht:appraise:remove")
    @Log(title = "商品评价", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(dAppraiseService.deleteDAppraiseByIds(ids));
    }

    /**
     * 查询商品一级分类
     */

    @RequiresPermissions("ht:appraise:goodlistone")
    @PostMapping("/goodlistone")
    @ResponseBody
    public List goodlistone(int type)
    {


        DCommodity commodity=new DCommodity();
        // dGoodssort.setParentId((long)0);
        List <DCommodity> list=idCommodityService.selectDCommodityList(commodity);

        return  list;
    }

}
