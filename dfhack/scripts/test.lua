
local crimes = df.global.world.criminal_cases.all

	for i=0, #crimes-1 do
		local crime = crimes[i]
		
		if crime.flags.discovered then
			print(dfhack.units.getVisibleName(crime.criminal))
		end

	end


