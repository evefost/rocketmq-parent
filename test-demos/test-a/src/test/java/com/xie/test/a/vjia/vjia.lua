local countKey=KEYS[1];
local orderKey=KEYS[2];
local orderId=ARGV[1];
local count= tonumber(redis.call('get',countKey) or 0);
if (count<1) then    
	return -1;
end
local orderIds=tostring(redis.call('get',orderKey) or '');
local i,j=string.find(orderIds,orderId);
if(i==nil) then    
	return 0;
end
local newOrderIds=string.gsub(orderIds,'_'..orderId,'');
redis.call('set',orderKey,newOrderIds);
redis.call('decr',countKey);
return 1;