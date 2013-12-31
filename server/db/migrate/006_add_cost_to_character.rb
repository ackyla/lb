class AddCostToCharacter < ActiveRecord::Migration
  def self.up
    change_table :characters do |t|
      t.integer :cost
    end
  end

  def self.down
    change_table :characters do |t|
      t.remove :cost
    end
  end
end
