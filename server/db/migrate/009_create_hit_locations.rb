class CreateHitLocations < ActiveRecord::Migration
  def self.up
    create_table :hit_locations do |t|
      t.float :latitude
      t.float :longitude
      t.references :user
      t.references :room
      t.references :target
      t.timestamps
    end
  end

  def self.down
    drop_table :hit_locations
  end
end
