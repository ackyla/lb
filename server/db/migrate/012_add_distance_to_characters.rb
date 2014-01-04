class AddDistanceToCharacters < ActiveRecord::Migration
  def self.up
    change_table :characters do |t|
      t.float :distance, default: 10000.0, null: false
    end
  end

  def self.down
    change_table :characters do |t|
      t.remove :distance
    end
  end
end
