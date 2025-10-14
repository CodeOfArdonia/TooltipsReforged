# Tooltips Reforged

This is a fork of [EnhancedTooltips](https://modrinth.com/mod/enhancedtooltips), remake the whole system and add new
features for both players and developers.

## Features

**All features are configurable (config entry is in mod menu).**

### Features for players

- New item name render and rarity & item icon
- Item zooming
- Effects for food, potion and tipped arrows
- New durability bar
- Banner Pattern & Map & Paint preview
- Armor preview for equipments
- Entity preview for spawn eggs and bucketed entities
- Badge displaying which mod provide this item
- Enchantment targets and descriptions (You need to install `Enchantment Description` to display for vanilla
  enchantments)
- Container preview
- Scrollable
- Backport 1.21.2+ texture features

### Features for developers

**You need to open advanced tooltip (F3+H) to use**

- Ctrl: Display item tags
- Alt: Display raw nbt/components & spawn egg entity info
- Shift: Specific information

For block, it will display block tag.

For spawn egg, it will display entity tag.

**Note: These keybindings can be changed in options. If set to unbind the corresponding data will be hide.**

### Known capability issues

- When some EMF resource packs installed (Such as `DetailedAnimations`), the spawn egg preview will meet some issues.
  You can close it in config.

### Developer Usage

Create a class implement `TooltipsReforgeEntrypoint`, then annotate with
`@EntryPointProvider(slug="tooltips_reforged")`(forge) or write into entrypoint `tooltips_reforged`(fabric)

**If you have any idea, submit on GitHub!**