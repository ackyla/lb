class CreateDetections < ActiveRecord::Migration
  def self.up
    create_table :detections do |t|
      t.references :location
      t.references :territory
      t.timestamps
    end
  end

  def self.down
    drop_table :detections
  end
end
