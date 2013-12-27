class DropRoomsTable < ActiveRecord::Migration
  def self.up
    drop_table :rooms
  end

  def self.down
  end
end
