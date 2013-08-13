class AddRoomToUser < ActiveRecord::Migration
  def self.up
    change_table :users do |t|
      t.references :room
    end
  end

  def self.down
    change_table :users do |t|
      t.remove :room
    end
  end
end
