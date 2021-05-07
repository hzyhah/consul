package com.cs.apiSer.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs.apiSer.dao.DoctorMapper;
import com.cs.apiSer.vo.Doctor;
import com.cs.apiSer.service.IDoctorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author houzy
 * @since 2021-04-15
 */

@Service
@Transactional
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements IDoctorService {

}
