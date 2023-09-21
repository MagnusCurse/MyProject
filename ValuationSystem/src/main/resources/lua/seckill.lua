-- 1. 参数列表
-- 1.1 优惠券 id
local voucherId = ARGV[1]
-- 1.2 用户 id
local userId = ARGV[1]

-- 2. 数据 key
-- 2.1 库存 key (lua 脚本连接字符串 ..)
local stockKey = 'seckill:stock:' .. voucherId
-- 2.2 订单 key
local orderKey = 'seckill:order:' .. userId

--3. lua 脚本业务
--3.1 判断库存是否充足
if(tonumber(redis.call('get',stockKey)) <= 0) then
    -- 3.2 库存不足, 返回 1
    return 1
end
-- 3.2 判断用户是否已经下单
if(redis.call('sismember',orderKey,userId) == 1) then
    -- 3.3 重复下单, 返回 2
    return 2
end
-- 3.4 扣减库存
redis.call('incrby', stockKey, -1)
-- 3.5 下单 (保存用户)
redis.call('sadd', orderKey, userId)
return 0
