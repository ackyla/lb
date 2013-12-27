class DropRoomsTable < ActiveRecord::Migration
  def self.up
    drop_table :rooms
    remove_column :users, :room_id
    remove_column :locations, :room_id
  end

  def self.down
  end
end
