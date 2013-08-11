class AddRoomToUser < ActiveRecord::Migration
  def self.up
    change_table :users do |t|
      t.integer :room_id
    end
  end

  def self.down
    change_table :users do |t|
      t.remove :room_id
    end
  end
end
