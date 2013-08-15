class RenameHitLocationToHit < ActiveRecord::Migration
  def self.up
    create_table :hits do |t|
      t.float :latitude
      t.float :longitude
      t.references :user
      t.references :room
      t.timestamps
    end
    drop_table :hit_locations
  end

  def self.down
  end
end
