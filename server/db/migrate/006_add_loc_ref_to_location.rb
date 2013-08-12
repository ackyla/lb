class AddLocRefToLocation < ActiveRecord::Migration
  def self.up
    change_table :locations do |t|
      t.references :user
      t.references :room
    end
  end

  def self.down
    change_table :locations do |t|
      t.remove :user_id
      t.remove :room_id
    end
  end
end
