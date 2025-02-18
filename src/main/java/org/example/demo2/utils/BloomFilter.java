package org.example.demo2.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class BloomFilter {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String BLOOM_FILTER_KEY = "bloomFilter:taskId";

    // 定义位图的大小（可根据预期数据量和误判率来设置，此处设为2^24位）
    private static final int BIT_SIZE = 1 << 10;

    // 使用的哈希函数数量
    private static final int HASH_FUNCTION_COUNT = 3;

    /**
     * 添加元素到布隆过滤器
     *
     * @param value 要添加的字符串
     */
    public void add(String value) {
        int[] offsets = getHashOffsets(value);
        for (int offset : offsets) {
            // Redis的setBit命令将指定位置设为true
            redisTemplate.opsForValue().setBit(BLOOM_FILTER_KEY, offset, true);
        }
    }

    /**
     * 判断元素是否存在于布隆过滤器中
     *
     * @param value 待判断的字符串
     * @return 如果存在返回true，否则返回false
     */
    public boolean contains(String value) {
        int[] offsets = getHashOffsets(value);
        for (int offset : offsets) {
            // Redis的getBit命令检查指定位置的值
            Boolean bit = redisTemplate.opsForValue().getBit(BLOOM_FILTER_KEY, offset);
            if (bit == null || !bit) {
                return false; // 只要有一个位置为0，该元素肯定不存在
            }
        }
        return true; // 所有位置都为1，则该元素可能存在
    }

    /**
     * 根据元素生成多个哈希值对应的位图偏移量
     *
     * @param value 待处理的字符串
     * @return 数组中存放了HASH_FUNCTION_COUNT个哈希结果对应的位下标
     */
    private int[] getHashOffsets(String value) {
        int[] offsets = new int[HASH_FUNCTION_COUNT];
        for (int i = 0; i < HASH_FUNCTION_COUNT; i++) {
            // 这里调用一个简单的哈希函数，不同的seed保证了哈希函数的多样性
            int hash = hash(value, i);
            // 将hash值映射到位数组的范围内（取绝对值和取模）
            offsets[i] = Math.abs(hash % BIT_SIZE);
        }
        return offsets;
    }

    /**
     * 一个简单的哈希函数实现，根据不同的seed得到不同的hash结果
     *
     * @param value 待哈希的字符串
     * @param seed  种子值，用于产生不同的哈希结果
     * @return 哈希值
     */
    private int hash(String value, int seed) {
        int result = 0;
        for (char c : value.toCharArray()) {
            result = seed * result + c;
        }
        return result;
    }

}
