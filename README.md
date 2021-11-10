# Carpet Shadow
carpet extension to fix various bugs related to shadow item stacks

## Carpet Settings

 - **shadowItemPersistence**

    prevents shadow item instances from unlinking/duping when saved to disk
    
 - **shadowItemTooltip**

    shows the shadow stack unique identifier when hoovering over a shadow stack with the mouse
    
 - **shadowItemFragilityFixes**

    various fixes to item related actions that might unlink/dupe/delete the shadow stacks
    
 - **shadowItemPreventCombine**

    by default same instances of the same shadow stack wont merge on inventory actions, this option overrides that behavior and prevents merging operations between any shadow stack.
    
    **PS:** *this option only has effect if **shadowItemFragilityFixes** is active*
    
 - **shadowItemIdSize**

    changes the length of the shadow stacks unique identifiers
     **PS:** *do not edit unless you know what you're doing*

## Feature List

 - [x] Shadow Item Persistence
	 - [x] Server Restarts
	 - [x] Player Join Leave
	 - [x] Chunk Unload/Reload
	 - [x] ShulkerBox Break/Place
	 - [x] Bundle add/remove ( To be tested )
 - [x] Fragility Fixes
	 - [x] Player Pick-Up shadow Stacks from Item Entities
	 - [x] Mouse Pickup and Place shadow stacks
	 - [x] Shift Click shadow stacks ( will only transfer them entirely w/o merging )
	 - [x] Quick Craft with/to shadow stacks ( simply disallowed )
	 - [x] Hoppers
		 - [x] Fail to pull from shadow stacks
	 - [x] Droppers
		 - [x] Unlinking on transfer to Inventory

## Known Bugs
 - [ ]  **General**
	 - [ ] **MINOR**: this mod only tracks shadow items that get generated with update suppression and item sawp operaion, if another method of generating a shadow item is found this mod will not work.
 - [ ]  **Persistence**
	 - [ ] **MINOR**: when the mod generates a new shadow id it only performs a check on the currently loaded IDs so there is a small chance of overlapping IDs
	 - [ ] **MINOR**: if two instances of a shadow item get unloaded with different amounts, the stack count will be the one of the first instance to get loaded back
 - [ ]  **Fragility**
	 - [ ] **MAJOR**: opening creative inventory will unlink and duplicate all the shadow stacks in the current inventory
 - [ ]  **Tooltips**
	 - [ ] **MINOR**: on servers the tooltips are sent to the clients as LORE nbt tags, this mod will strip them down on the clientside but non modded clients will see some de-synced lores while performing item movements/splits in inventories
	 - [ ] **MAJOR (Unconfirmed)**: non modded creative inventory might behave strangely if tooltips are active on server

    

