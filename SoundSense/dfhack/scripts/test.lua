local buildings = df.global.world.buildings.all

for i=0, #buildings-1 do
	local building = buildings[i]
print(building)
	if type(building) == "building_workshopst" then
	print(type(building))
for key,value in pairs(building) do
    print("found member " .. key);
end
for key,value in pairs(building.flags) do
    print("found member " .. key);
end
	end
end