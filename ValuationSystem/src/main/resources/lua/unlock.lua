
-- 这里的 KEYS[1] 就是锁中的 key, 而 ARGV[1] 就是线程标识
-- 获取线程标识
local id = redis.call('get',KEYS[1])
-- 比较线程标识与锁中的标识是否一致
if(id == ARGV[1]) then
    -- 释放锁
    return redis.call('del', KEYS[1])
end
return 0
