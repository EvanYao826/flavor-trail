package com.flavor.trail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flavor.trail.entity.ExploreProgress;
import com.flavor.trail.entity.Province;
import com.flavor.trail.mapper.ExploreProgressMapper;
import com.flavor.trail.mapper.ProvinceMapper;
import com.flavor.trail.service.ExploreService;
import com.flavor.trail.vo.ExploreProgressVO;
import com.flavor.trail.vo.ExploreStatsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExploreServiceImpl implements ExploreService {

    private final ExploreProgressMapper exploreProgressMapper;
    private final ProvinceMapper provinceMapper;

    public ExploreServiceImpl(ExploreProgressMapper exploreProgressMapper, ProvinceMapper provinceMapper) {
        this.exploreProgressMapper = exploreProgressMapper;
        this.provinceMapper = provinceMapper;
    }

    @Override
    public List<ExploreProgressVO> getProgress(Long userId) {
        LambdaQueryWrapper<ExploreProgress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExploreProgress::getUserId, userId);
        List<ExploreProgress> progresses = exploreProgressMapper.selectList(wrapper);

        Map<Long, Province> provinceMap = provinceMapper.selectList(null).stream()
                .collect(Collectors.toMap(Province::getId, p -> p));

        return progresses.stream()
                .map(p -> ExploreProgressVO.builder()
                        .provinceId(p.getProvinceId())
                        .provinceName(provinceMap.getOrDefault(p.getProvinceId(), new Province()).getName())
                        .isExplored(p.getIsExplored() == 1)
                        .foodViewedCount(p.getFoodViewedCount())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ExploreStatsVO getStats(Long userId) {
        LambdaQueryWrapper<ExploreProgress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExploreProgress::getUserId, userId)
                .eq(ExploreProgress::getIsExplored, 1);
        int exploredCount = (int) exploreProgressMapper.selectCount(wrapper);

        int totalCount = (int) provinceMapper.selectCount(null);
        float percentage = totalCount > 0 ? (float) exploredCount / totalCount * 100 : 0;

        return ExploreStatsVO.builder()
                .exploredCount(exploredCount)
                .totalCount(totalCount)
                .percentage(percentage)
                .build();
    }

    @Override
    public void updateProgress(Long userId, Long provinceId) {
        LambdaQueryWrapper<ExploreProgress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExploreProgress::getUserId, userId)
                .eq(ExploreProgress::getProvinceId, provinceId);

        ExploreProgress progress = exploreProgressMapper.selectOne(wrapper);

        if (progress == null) {
            progress = ExploreProgress.builder()
                    .userId(userId)
                    .provinceId(provinceId)
                    .isExplored(1)
                    .foodViewedCount(1)
                    .firstExploredAt(LocalDateTime.now())
                    .lastExploredAt(LocalDateTime.now())
                    .build();
            exploreProgressMapper.insert(progress);
        } else {
            progress.setFoodViewedCount(progress.getFoodViewedCount() + 1);
            progress.setIsExplored(1);
            progress.setLastExploredAt(LocalDateTime.now());
            exploreProgressMapper.updateById(progress);
        }
    }
}