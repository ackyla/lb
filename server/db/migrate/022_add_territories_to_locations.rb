class AddTerritoriesToLocations < ActiveRecord::Migration
  def self.up
    change_table :locations do |t|
      t.references :territories
    end
  end

  def self.down
    change_table :locations do |t|
      t.remove :territories
    end
  end
end
